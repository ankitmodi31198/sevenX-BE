package com.backend.sevenX.data.dto.responseDto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ListContactFormResDto {

    private List<ContactFormResDto> contactList;

    private Integer totalContactsCount;
}
