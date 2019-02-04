package pl.wturnieju.controller.dto.tournament;

import org.mapstruct.Mapper;

import pl.wturnieju.tournament.Member;

@Mapper(componentModel = "spring")
public interface MemberDtoMapper {

    MemberDto mapToMemberDto(Member member);
}
