package pl.wturnieju.search;

import java.util.List;

public interface ISearch<I, O> {

    List<O> find(I input);
}
