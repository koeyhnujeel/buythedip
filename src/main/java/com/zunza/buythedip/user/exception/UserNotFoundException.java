package com.zunza.buythedip.user.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class UserNotFoundException extends CustomException {

    private static final String MESSAGE = "존재하지 않는 사용자 입니다. ACCOUNT ID: ";

    public UserNotFoundException(String accountId) {
        super(MESSAGE + accountId);
    }

    @Override
    public int getStatusCode() {
      return HttpServletResponse.SC_NOT_FOUND;
    }
}
