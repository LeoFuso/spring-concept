package model.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import model.Persisted;

import javax.persistence.Entity;

@Slf4j
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Tag extends Persisted {

    private String name;
    private String description;

}
