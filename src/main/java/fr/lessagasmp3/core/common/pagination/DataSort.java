package fr.lessagasmp3.core.common.pagination;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class DataSort {

    private boolean sorted;
    private boolean unsorted;
    private boolean empty;

}
