package mate.academy.demo.mapper;

import mate.academy.demo.config.MapperConfig;
import mate.academy.demo.dto.attachment.AttachmentDto;
import mate.academy.demo.dto.attachment.CreateAttachmentRequestDto;
import mate.academy.demo.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AttachmentMapper {

    Attachment toModel(CreateAttachmentRequestDto requestDto);

    @Mapping(target = "taskId", source = "task.id")
    AttachmentDto toDto(Attachment attachment);
}
