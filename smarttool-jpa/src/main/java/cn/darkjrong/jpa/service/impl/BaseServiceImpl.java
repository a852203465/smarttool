package cn.darkjrong.jpa.service.impl;

import cn.darkjrong.jpa.repository.BaseRepository;
import cn.darkjrong.jpa.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * service层通用方法实现
 *
 * @author Rong.Jia
 * @date 2021/12/20
 */
@Slf4j
public class BaseServiceImpl<T> implements BaseService<T> {

    @Autowired
    private BaseRepository<T> baseRepository;

    @Override
    public T findById(Long id) {
        return baseRepository.getById(id);
    }

    @Override
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void delete(T entity) {
        baseRepository.delete(entity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteById(Long id) {
        baseRepository.deleteById(id);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public T insetNew(T entity) {
        return baseRepository.save(entity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public List<T> insetNewAll(List<T> entity){

        return baseRepository.saveAll(entity);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public T modify(T entity) {
        return baseRepository.saveAndFlush(entity);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {

        return baseRepository.findAll(pageable);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteInBatch(List<T> entities) {

        baseRepository.deleteAllInBatch(entities);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteAll() {
        baseRepository.deleteAll();
    }
}
