package pl.wturnieju.cli.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserInfoResponse extends CliResponse {
    List<UserInfoResponseItem> items;
}
