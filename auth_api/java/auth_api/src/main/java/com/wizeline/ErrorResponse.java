package com.wizeline;

public class ErrorResponse {
  public final int status;
  public final String message;

  public ErrorResponse(int status, String message) {
    this.status = status;
    this.message = message;
  }

  @Override
  public String toString() {
    return "ErrorResponse{" +
            "status=" + status +
            ", message='" + message + '\'' +
            '}';
  }
}
