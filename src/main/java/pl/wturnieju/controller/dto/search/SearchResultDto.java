package pl.wturnieju.controller.dto.search;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResultDto<T> {

    private List<T> data;
}
