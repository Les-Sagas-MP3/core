package fr.lessagasmp3.core.common.pagination;

import fr.lessagasmp3.core.common.model.IdentityModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter(AccessLevel.PUBLIC)
@Setter(AccessLevel.PUBLIC)
public class DataPage<T> {

    private List<T> content = new ArrayList<>();
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private int numberOfElements;
    private DataSort sort;

    public DataPage() {}

    public DataPage(Page<? extends IdentityModel> entities) {
        totalPages = entities.getTotalPages();
        totalElements = entities.getTotalElements();
        number = entities.getNumber();
        size = entities.getSize();
        numberOfElements = entities.getNumberOfElements();
        sort = new DataSort();
        sort.setEmpty(entities.getSort().isEmpty());
        sort.setSorted(entities.getSort().isSorted());
        sort.setUnsorted(entities.getSort().isUnsorted());
    }

}
