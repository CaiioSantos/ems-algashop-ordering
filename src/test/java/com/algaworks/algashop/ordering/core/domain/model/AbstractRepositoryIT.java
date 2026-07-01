package com.algaworks.algashop.ordering.core.domain.model;

import com.algaworks.algashop.ordering.utils.TestContainerPostgresSqlConfig;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerPostgresSqlConfig.class)
public class AbstractRepositoryIT {
}