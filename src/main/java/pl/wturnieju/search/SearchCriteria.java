package pl.wturnieju.search;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {

    private String text;

    private Integer limit;
}
