package pl.wturnieju.cli.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse extends CliResponse {
    List<UserInfoResponseItem> items;
}
