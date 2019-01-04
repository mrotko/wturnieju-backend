package pl.wturnieju.cli.dto;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserInfoResponse extends CliResponse {
    List<UserInfoResponseItem> items;
}
