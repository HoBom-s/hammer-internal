package com.hammer.internal.notification.application.dto;

public record CreateTemplateCommand(String templateKey, String titleTemplate, String bodyTemplate, String channel) {}
