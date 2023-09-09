package com.example.springboot.exception;

import com.example.springboot.constant.ErrorMessage;
import com.example.springboot.dto.request.SignUpRequestDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
@Slf4j
public class ExceptionTranslator {
    private static final String ERROR_CODE_KEY = "errorCode";
    private static final String MESSAGE_KEY = "message";

    /**
     * Exception handling when the json body in request is malformed
     *
     * @param exception : HttpMessageNotReadableException
     * @return : The response entity
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(ERROR_CODE_KEY, ErrorMessage.COMMON_JSON_BODY_MALFORMED.getErrorCode());
        response.put(MESSAGE_KEY, ErrorMessage.COMMON_JSON_BODY_MALFORMED.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    /**
     * Exception handling when input request parameter is invalid.
     *
     * @param ex: MethodArgumentNotValidException
     * @return : The response entity
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleAMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        String errorMessageName = result.getAllErrors().get(0).getDefaultMessage();
        String errorField = ((FieldError) result.getAllErrors().get(0)).getField();

        if (ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.name().equals(errorMessageName)) {
            String loginName = ((SignUpRequestDTO) Objects.requireNonNull(result.getTarget())).getLoginName();
            response.put(ERROR_CODE_KEY, ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.getErrorCode());
            response.put(MESSAGE_KEY, String.format(ErrorMessage.SIGNUP_LOGIN_NAME_DUPLICATE.getMessage(), loginName));
        } else if (ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.name().equals(errorMessageName)) {
            String emailAddress = ((SignUpRequestDTO) Objects.requireNonNull(result.getTarget())).getEmailAddress();
            response.put(ERROR_CODE_KEY, ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.getErrorCode());
            response.put(MESSAGE_KEY, String.format(ErrorMessage.SIGNUP_EMAIL_ADDRESS_DUPLICATE.getMessage(), emailAddress));
        } else if (ErrorMessage.COMMON_FIELD_REQUIRED.name().equals(errorMessageName)) {
            response.put(ERROR_CODE_KEY, ErrorMessage.COMMON_FIELD_REQUIRED.getErrorCode());
            response.put(MESSAGE_KEY, String.format(ErrorMessage.COMMON_FIELD_REQUIRED.getMessage(), errorField));
        } else {
            // Message of ErrorMessage do not have any argument
            Arrays.asList(ErrorMessage.values()).forEach(
                    (errorMessage -> {
                        if (errorMessage.name().equals(errorMessageName)) {
                            response.put(ERROR_CODE_KEY, errorMessage.getErrorCode());
                            response.put(MESSAGE_KEY, errorMessage.getMessage());
                        }
                    })
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     *
     * Exception handling when username was not found.
     *
     * @param exception: UsernameNotFoundException
     * @return : The response entity
     */
    @ExceptionHandler({UsernameNotFoundException.class})
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(ERROR_CODE_KEY, ErrorMessage.LOGIN_NAME_NOT_FOUND.getErrorCode());
        response.put(MESSAGE_KEY, String.format(ErrorMessage.LOGIN_NAME_NOT_FOUND.getMessage(), exception.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<?> handleUsernameNotFoundException(UserNotFoundException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(ERROR_CODE_KEY, ErrorMessage.COMMON_USER_NOT_FOUND.getErrorCode());
        response.put(MESSAGE_KEY, String.format(ErrorMessage.COMMON_USER_NOT_FOUND.getMessage(), exception.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    /**
     * Exception handling when refresh token and access token expired
     *
     * @return : The response entity
     */
    @ExceptionHandler({RefreshTokenExpiredException.class, ExpiredJwtException.class})
    public ResponseEntity<?> handleRefreshTokenExpiredException() {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(ERROR_CODE_KEY, ErrorMessage.LOGIN_TOKEN_EXPIRED.getErrorCode());
        response.put(MESSAGE_KEY, ErrorMessage.LOGIN_TOKEN_EXPIRED.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Exception handling when the login name and password are wrong.
     *
     * @param exception : The BadCredentialsException
     * @return : The response entity
     */
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(ERROR_CODE_KEY, ErrorMessage.LOGIN_BAD_CREDENTIALS.getErrorCode());
        response.put(MESSAGE_KEY, ErrorMessage.LOGIN_BAD_CREDENTIALS.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Exception handling when the user does not have permissions to access the source.
     *
     * @param exception : AccessDeniedException
     * @return : The response entity
     */
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(ERROR_CODE_KEY, ErrorMessage.LOGIN_ACCESS_DENIED.getErrorCode());
        response.put(MESSAGE_KEY, ErrorMessage.LOGIN_ACCESS_DENIED.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Exception handling when The JWT access token is Malformed
     *
     * @param exception : MalformedJwtException
     * @return : The response entity
     */
    @ExceptionHandler({SignatureException.class, MalformedJwtException.class})
    public ResponseEntity<?> handleSignatureException(SignatureException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(ERROR_CODE_KEY, ErrorMessage.LOGIN_WRONG_TOKEN.getErrorCode());
        response.put(MESSAGE_KEY, ErrorMessage.LOGIN_WRONG_TOKEN.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

    /**
     * Exception handling when the verification code is not valid or expired.
     *
     * @param exception : The VerificationException
     * @return : The response entity
     */
    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<?> handleVerificationException(VerificationException exception) {
        LinkedHashMap<String, String> response = new LinkedHashMap<>();
        response.put(ERROR_CODE_KEY, ErrorMessage.VERIFY_NOT_ACCEPTABLE.getErrorCode());
        response.put(MESSAGE_KEY, ErrorMessage.VERIFY_NOT_ACCEPTABLE.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    // Uncomment bên dưới khi project hoàn thành

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleAllException(Exception ex) {
//        ErrorMessage errorMessage = ErrorMessage.COMMON_INTERNAL_SERVER_ERROR;
//        LinkedHashMap<String, String> response = new LinkedHashMap<>();
//        response.put(ERROR_CODE_KEY, errorMessage.getErrorCode());
//        response.put(MESSAGE_KEY, errorMessage.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(response);
//    }

}
