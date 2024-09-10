package com.gold.api.gold_api.grpc;

import com.gold.api.gold_api.global.error.ErrorCode;
import com.gold.api.gold_api.global.exception.CustomException;
import com.gold.proto.AuthServiceGrpc;
import com.gold.proto.JoinRequestDto;
import com.gold.proto.JoinResponseDto;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

public class AuthServiceCaller {

    private final AuthServiceGrpc.AuthServiceBlockingStub blockingStub;

    public AuthServiceCaller(ManagedChannel channel) {
        this.blockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public JoinResponseDto sendJoinUser(JoinRequestDto joinGrpc) {
        try {
            // 서버로 회원가입 요청 전송
            return blockingStub.registerUser(joinGrpc);
        } catch (StatusRuntimeException e) {
            // gRPC 에러 핸들링
            System.err.println("Error during registration: " + e.getStatus().getCode());
            System.err.println("Error message: " + e.getStatus().getDescription());

            // 에러 코드에 따른 추가 처리 로직
            switch (e.getStatus().getCode()) {
                case INVALID_ARGUMENT:
                    System.out.println("회원가입 요청이 잘못되었습니다. 입력을 확인하세요.");
                    throw new CustomException(ErrorCode.DATE_FORMAT_ERROR);
                case ALREADY_EXISTS:
                    System.out.println("이미 존재하는 사용자 ID입니다.");
                    throw new CustomException(ErrorCode.USER_ALREADY_EXIST);

            }
            return null;
        }

    }

}