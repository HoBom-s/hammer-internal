package com.hammer.internal.notification.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.hammer.internal.common.domain.NotFoundException;
import com.hammer.internal.notification.application.port.out.DeleteTemplatePort;
import com.hammer.internal.notification.application.port.out.LoadTemplatePort;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteTemplateServiceTest {

    @Mock
    LoadTemplatePort loadTemplatePort;

    @Mock
    DeleteTemplatePort deleteTemplatePort;

    @InjectMocks
    DeleteTemplateService sut;

    @Test
    void deletes_when_exists() {
        UUID id = UUID.randomUUID();
        given(loadTemplatePort.existsById(id)).willReturn(true);

        sut.delete(id);

        then(deleteTemplatePort).should().deleteById(id);
    }

    @Test
    void throws_not_found_when_absent() {
        UUID id = UUID.randomUUID();
        given(loadTemplatePort.existsById(id)).willReturn(false);

        assertThatThrownBy(() -> sut.delete(id)).isInstanceOf(NotFoundException.class);
        then(deleteTemplatePort).should(never()).deleteById(any());
    }
}
