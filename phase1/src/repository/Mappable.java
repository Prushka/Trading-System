package repository;

import java.util.List;

public interface Mappable {

    List<String> getHeader();

    List<String> toList();
}
