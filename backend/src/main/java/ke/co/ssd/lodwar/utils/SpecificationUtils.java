package ke.co.ssd.lodwar.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpecificationUtils {

    @SafeVarargs
    public static Predicate toPredicateConditionAND(final CriteriaBuilder criteriaBuilder, final Optional<Predicate>... inputPredicates) {

        final List<Predicate> resultPredicates = Stream
            .of(inputPredicates)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        Predicate result = null;

        for (final Predicate predicate : resultPredicates) {

            if (Objects.nonNull(predicate)) {
                result = Objects.isNull(result) ? predicate : criteriaBuilder.and(result, predicate);
            }
        }

        return result;
    }

}
