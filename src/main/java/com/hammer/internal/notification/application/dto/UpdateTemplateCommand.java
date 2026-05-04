package com.hammer.internal.notification.application.dto;

public record UpdateTemplateCommand(String templateKey, String titleTemplate, String bodyTemplate, String channel) {}
