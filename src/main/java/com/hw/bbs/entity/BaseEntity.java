package com.hw.bbs.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * <p> 抽象实体基类，提供统一的ID，和相关的基本功能方法
 * 如果是oracle请参考{@link BaseOracleEntity}
 * <p>User: 郭玉昆
 * <p>Date: 13-1-12 下午4:05
 * <p>Version: 1.0
 */
@MappedSuperclass
public class BaseEntity<ID extends Serializable> extends AbstractEntity<ID> {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
	@Transient
    private ID id;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

}