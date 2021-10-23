package br.com.erp.converter.financialrecord;

import br.com.erp.api.Tag;
import br.com.erp.api.financialrecord.FinancialRecord;
import br.com.erp.entity.FinancialRecordEntity;
import br.com.erp.entity.TagEntity;
import br.com.erp.entity.UserEntity;
import br.com.erp.exception.NotFoundException;
import br.com.erp.repository.FinancialRecordRepository;
import br.com.erp.repository.TagRepository;
import br.com.erp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.function.Function;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class FinancialRecordToFinancialRecordEntity implements Function<FinancialRecord, FinancialRecordEntity> {

    private final TagRepository tagRepository;

    private final FinancialRecordRepository financialRecordRepository;            ;

    private final UserService userService;

    @Override
    public FinancialRecordEntity apply(FinancialRecord financialRecord) {
        var user = userService.getCurrentUser();

        return new FinancialRecordEntity(
                financialRecord.id(),
                financialRecord.name(),
                financialRecord.details(),
                financialRecord.value(),
                financialRecord.type(),
                financialRecord.date(),
                ofNullable(financialRecord.tags())
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(it ->
                                tagRepository.findByUserAndName(user, it.name())
                                .orElseGet(() -> saveTag(it, user)))
                        .collect(toList()),
                user,
                financialRecord.paid(),
                getCreatedAt(financialRecord.id(), user),
                now()
        );
    }

    private TagEntity saveTag(Tag tag, UserEntity user) {
        return tagRepository.save(new TagEntity(tag.id(), tag.name(), user));
    }

    private LocalDateTime getCreatedAt(Long id, UserEntity user) {
        return id == null ?
                now() :
                financialRecordRepository.findByUserAndId(user, id)
                        .orElseThrow(() -> new NotFoundException("Registro não encontrado"))
                        .getCreatedAt();
    }

}
