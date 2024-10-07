package com.project.TransportCardSystem.api.common.dto;


public record RestApiResponse<T>(boolean success, T data) {

}
