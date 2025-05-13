package com.zunza.buythedip.user.exception;

import com.zunza.buythedip.common.CustomException;

import jakarta.servlet.http.HttpServletResponse;

public class UserNotFoundException extends CustomException {

    private static final String ACCOUNT_MESSAGE = "존재하지 않는 사용자 입니다. ACCOUNT ID: ";
    private static final String ID_MESSAGE = "존재하지 않는 사용자 입니다. USER ID: ";

    public UserNotFoundException(String accountId) {
        super(ACCOUNT_MESSAGE + accountId);
    }

    public UserNotFoundException(Long userId) {
        super(ID_MESSAGE + userId);
    }

    @Override
    public int getStatusCode() {
      return HttpServletResponse.SC_NOT_FOUND;
    }
}
