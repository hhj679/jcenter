package com.hw.bbs.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hw.bbs.dao.BaseDao;
import com.hw.bbs.entity.AbstractEntity;

public abstract class BaseService<T extends AbstractEntity<Long>, ID extends Serializable> {

    private final Logger logger = LoggerFactory.getLogger(BaseService.class);

    /** 泛型对应的Class定义 */
    private Class<T> entityClass;

    /** 子类设置具体的DAO对象实例 */
    abstract protected BaseDao<T, ID> getEntityDao();

    @PersistenceContext
    private EntityManager entityManager;

    protected Class<T> getEntityClass() {
        if (entityClass == null) {
            try {
                // 通过反射取得Entity的Class.
                Object genericClz = getClass().getGenericSuperclass();
                if (genericClz instanceof ParameterizedType) {
                    entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                }
            } catch (Exception e) {
                logger.error("error detail:", e);
            }
        }
        return entityClass;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * 强制从数据库刷新加载实体对象
     * 主要用于Spring DATA JPA Modifying操作后强制refresh重新从数据库加载数据
     * @param entity
     */
    protected void foreceRefreshEntity(Object entity) {
        entityManager.refresh(entity);
    }

    /**
     * 创建数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * 
     * @param entity
     *            待创建数据对象
     */
    protected void preInsert(T entity) {

    }

    /**
     * 更新数据保存数据之前额外操作回调方法 默认为空逻辑，子类根据需要覆写添加逻辑即可
     * 
     * @param entity
     *            待更新数据对象
     */
    protected void preUpdate(T entity) {

    }

    /**
     * 数据保存操作
     * 
     * @param entity
     * @return
     */
    public T save(T entity) {
        if (entity.isNew()) {
            preInsert(entity);
        } else {
            preUpdate(entity);
        }
        getEntityDao().save(entity);
        return entity;
    }

    /**
     * 批量数据保存操作 其实现只是简单循环集合每个元素调用 {@link #save(Persistable)}
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     * 
     * @param entities
     *            待批量操作数据集合
     * @return
     */
    public List<T> save(Iterable<T> entities) {
        List<T> result = new ArrayList<T>();
        if (entities == null) {
            return result;
        }
        for (T entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    /**
     * 基于主键查询单一数据对象
     * 
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public T findOne(ID id) {
        Assert.notNull(id);
        return getEntityDao().findOne(id);
    }

    /**
     * 基于主键查询单一数据对象
     * 
     * @param id 主键
     * @param initLazyPropertyNames 需要预先初始化的lazy集合属性名称
     * @return
     * @throws Exception 
     */
    @Transactional(readOnly = true)
    public T findDetachedOne(ID id, String... initLazyPropertyNames) throws Exception {
        Assert.notNull(id);
        T entity = getEntityDao().findOne(id);
        if (initLazyPropertyNames != null && initLazyPropertyNames.length > 0) {
            for (String name : initLazyPropertyNames) {
                try {
                    Object propValue = MethodUtils.invokeMethod(entity, "get" + StringUtils.capitalize(name));
                    if (propValue != null && propValue instanceof Collection<?>) {
                        ((Collection<?>) propValue).size();
                    } else if (propValue != null && propValue instanceof Persistable<?>) {
                        ((Persistable<?>) propValue).getId();
                    }
                } catch (Exception e) {
                    throw new Exception("error.init.detached.entity", e);
                }
            }
        }
        getEntityManager().detach(entity);
        return entity;
    }

    /**
     * 基于主键集合查询集合数据对象
     * 
     * @param ids 主键集合
     * @return
     */
    @Transactional(readOnly = true)
    public List<T> findAll(final ID... ids) {
        Assert.isTrue(ids != null && ids.length > 0, "必须提供有效查询主键集合");
        Specification<T> spec = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                @SuppressWarnings("rawtypes")
                Path expression = root.get("id");
                return expression.in(ids);
            }
        };
        return this.getEntityDao().findAll(spec);
    }

    /**
     * 数据删除操作
     * 
     * @param entity
     *            待操作数据
     */
    public void delete(T entity) {
        getEntityDao().delete(entity);
    }

    /**
     * 批量数据删除操作 其实现只是简单循环集合每个元素调用 {@link #delete(Persistable)}
     * 因此并无实际的Batch批量处理，如果需要数据库底层批量支持请自行实现
     * 
     * @param entities
     *            待批量操作数据集合
     * @return
     */
    public void delete(Iterable<T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    /**
     * 根据泛型对象属性和值查询集合对象
     * 
     * @param property 属性名，即对象中数量变量名称
     * @param value 参数值
     */
    public List<T> findListByProperty(final String property, final Object value) {
        Specification<T> spec = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                @SuppressWarnings("rawtypes")
                Path expression = root.get(property);
                return builder.equal(expression, value);
            }
        };

        return this.getEntityDao().findAll(spec);
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
     * 
     * @param property 属性名，即对象中数量变量名称
     * @param value 参数值
     * @return 未查询到返回null，如果查询到多条数据则抛出异常
     */
    public T findByProperty(final String property, final Object value) {
        List<T> entities = findListByProperty(property, value);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        } else {
            Assert.isTrue(entities.size() == 1);
            return entities.get(0);
        }
    }

    /**
     * 根据泛型对象属性和值查询唯一对象
     * 
     * @param property 属性名，即对象中数量变量名称
     * @param value 参数值
     * @return 未查询到返回null，如果查询到多条数据则返回第一条
     */
    public T findFirstByProperty(final String property, final Object value) {
        List<T> entities = findListByProperty(property, value);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        } else {
            return entities.get(0);
        }
    }

    /**
     * 通用的对象属性和值查询接口，根据泛型参数确定返回类型数据
     * 
     * @param baseDao
     *            泛型参数对象DAO接口
     * @param property
     *            属性名，即对象中数量变量名称
     * @param value
     *            参数值
     * @return 未查询到返回null，如果查询到多条数据则抛出异常
     */
    public <X> X findByProperty(BaseDao<X, ID> baseDao, final String property, final Object value) {
        Specification<X> spec = new Specification<X>() {
            @Override
            public Predicate toPredicate(Root<X> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                String[] names = StringUtils.split(property, ".");
                @SuppressWarnings("rawtypes")
                Path expression = root.get(names[0]);
                for (int i = 1; i < names.length; i++) {
                    expression = expression.get(names[i]);
                }
                return builder.equal(expression, value);
            }
        };
        List<X> entities = baseDao.findAll(spec);
        if (CollectionUtils.isEmpty(entities)) {
            return null;
        } else {
            Assert.isTrue(entities.size() == 1);
            return entities.get(0);
        }
    }

    public String toSql(Criteria criteria) {
        CriteriaImpl criteriaImpl = (CriteriaImpl) criteria;
        SessionImplementor session = criteriaImpl.getSession();
        SessionFactoryImplementor factory = session.getFactory();
        CriteriaQueryTranslator translator = new CriteriaQueryTranslator(factory, criteriaImpl, criteriaImpl.getEntityOrClassName(),
                CriteriaQueryTranslator.ROOT_SQL_ALIAS);
        String[] implementors = factory.getImplementors(criteriaImpl.getEntityOrClassName());

        CriteriaJoinWalker walker = new CriteriaJoinWalker((OuterJoinLoadable) factory.getEntityPersister(implementors[0]), translator, factory,
                criteriaImpl, criteriaImpl.getEntityOrClassName(), session.getLoadQueryInfluencers());

        String sql = walker.getSQLString();
        return sql;
    }

    /**
     * 基于Native SQL和分页(不含排序，排序直接在native sql中定义)对象查询数据集合
     * 
     * @param pageable 分页(不含排序，排序直接在native sql中定义)对象
     * @param sql Native SQL(自行组装好动态条件和排序的原生SQL语句，不含order by部分)
     * @return Map结构的集合分页对象
     */
    @Transactional(readOnly = true)
    public Page<Map> findByPageNativeSQL(Pageable pageable, String sql) {
        return findByPageNativeSQL(pageable, sql, null);
    }

    /**
     * 基于Native SQL和分页(不含排序，排序直接在native sql中定义)对象查询数据集合
     * 
     * @param pageable 分页(不含排序，排序直接在native sql中定义)对象
     * @param sql Native SQL(自行组装好动态条件和排序的原生SQL语句，不含order by部分)
     * @param orderby order by部分
     * @return Map结构的集合分页对象
     */
    @Transactional(readOnly = true)
    public Page<Map> findByPageNativeSQL(Pageable pageable, String sql, String orderby) {
        Query query = null;
        if (StringUtils.isNotBlank(orderby)) {
            query = getEntityManager().createNativeQuery(sql + " " + orderby);
        } else {
            query = getEntityManager().createNativeQuery(sql);
        }
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        Query queryCount = getEntityManager().createNativeQuery("select count(*) from (" + sql + ") cnt");
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        Object count = queryCount.getSingleResult();
        return new PageImpl(query.getResultList(), pageable, Long.valueOf(count.toString()));
    }

    /**
     * 基于JPA通用的查询条件count记录数据
     * 
     * @param spec
     * @return
     */
    @Transactional(readOnly = true)
    private long count(Specification<T> spec) {
        return getEntityDao().count(spec);
    }

    private Expression parseExpr(Root<?> root, CriteriaBuilder criteriaBuilder, String expr, Map<String, Expression<?>> parsedExprMap) {
        if (parsedExprMap == null) {
            parsedExprMap = Maps.newHashMap();
        }
        Expression<?> expression = null;
        if (expr.indexOf("(") > -1) {
            int left = 0;
            char[] chars = expr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '(') {
                    left = i;
                }
            }
            String leftStr = expr.substring(0, left);
            String op = null;
            char[] leftStrs = leftStr.toCharArray();
            for (int i = leftStrs.length - 1; i > 0; i--) {
                if (leftStrs[i] == '(' || leftStrs[i] == ')' || leftStrs[i] == ',') {
                    op = leftStr.substring(i + 1);
                    break;
                }
            }
            if (op == null) {
                op = leftStr;
            }
            String rightStr = expr.substring(left + 1);
            String arg = StringUtils.substringBefore(rightStr, ")");
            String[] args = arg.split(",");
            //logger.debug("op={},arg={}", op, arg);
            if (op.equalsIgnoreCase("case")) {
                Case selectCase = criteriaBuilder.selectCase();

                Expression caseWhen = parsedExprMap.get(args[0]);

                String whenResultExpr = args[1];
                Object whenResult = parsedExprMap.get(whenResultExpr);
                if (whenResult == null) {
                    Case<Long> whenCase = selectCase.when(caseWhen, new BigDecimal(whenResultExpr));
                    selectCase = whenCase;
                } else {
                    Case<Expression<?>> whenCase = selectCase.when(caseWhen, whenResult);
                    selectCase = whenCase;
                }
                String otherwiseResultExpr = args[2];
                Object otherwiseResult = parsedExprMap.get(otherwiseResultExpr);
                if (otherwiseResult == null) {
                    expression = selectCase.otherwise(new BigDecimal(otherwiseResultExpr));
                } else {
                    expression = selectCase.otherwise((Expression<?>) otherwiseResult);
                }
            } else {
                Object[] subExpressions = new Object[args.length];
                for (int i = 0; i < args.length; i++) {
                    subExpressions[i] = parsedExprMap.get(args[i]);
                    if (subExpressions[i] == null) {
                        String name = args[i];
                        try {
                            Path<?> item = null;
                            if (name.indexOf(".") > -1) {
                                String[] props = StringUtils.split(name, ".");
                                item = root.get(props[0]);
                                for (int j = 1; j < props.length; j++) {
                                    item = item.get(props[j]);
                                }
                            } else {
                                item = root.get(name);
                            }
                            subExpressions[i] = (Expression) item;
                        } catch (Exception e) {
                            subExpressions[i] = new BigDecimal(name);
                        }
                    }
                }
                try {
                    //criteriaBuilder.quot();
                    expression = (Expression) MethodUtils.invokeMethod(criteriaBuilder, op, subExpressions);
                } catch (Exception e) {
                    logger.error("Error for aggregate  setting ", e);
                }
            }

            String exprPart = op + "(" + arg + ")";
            String exprPartConvert = exprPart.replace(op + "(", op + "_").replace(arg + ")", arg + "_").replace(",", "_");
            expr = expr.replace(exprPart, exprPartConvert);
            parsedExprMap.put(exprPartConvert, expression);

            if (expr.indexOf("(") > -1) {
                expression = parseExpr(root, criteriaBuilder, expr, parsedExprMap);
            }
        } else {
            String name = expr;
            Path<?> item = null;
            if (name.indexOf(".") > -1) {
                String[] props = StringUtils.split(name, ".");
                item = root.get(props[0]);
                for (int j = 1; j < props.length; j++) {
                    item = item.get(props[j]);
                }
            } else {
                item = root.get(name);
            }
            expression = item;
        }
        return expression;
    }

    private String fixCleanAlias(String name) {
        return StringUtils.remove(StringUtils.remove(StringUtils.remove(StringUtils.remove(StringUtils.remove(name, "("), ")"), "."), ","), "-");
    }

    private Expression<?> buildExpression(Root<?> root, CriteriaBuilder criteriaBuilder, String name, String alias) {
        Expression<?> expr = parseExpr(root, criteriaBuilder, name, null);
        if (alias != null) {
            expr.alias(alias);
        }
        return expr;
    }


    private Selection<?>[] mergeSelections(Root<?> root, Selection<?>[] path1, Selection<?>... path2) {
        Selection<?>[] parsed = new Selection<?>[path1.length + path2.length];
        int i = 0;
        for (Selection<?> path : path1) {
            parsed[i++] = path;
        }
        for (Selection<?> path : path2) {
            parsed[i++] = path;
        }
        return parsed;
    }

    /**
     * 供子类调用的关联对象关联关系操作辅助方法
     * 
     * @param id
     *            当前关联主对象主键，如User对象主键
     * @param r2EntityIds
     *            关联目标对象的主键集合，如用户关联角色的Role对象集合的主键
     * @param r2PropertyName
     *            主对象中关联集合对象属性的名称，如User对象中定义的userR2Roles属性名
     * @param r2EntityPropertyName
     *            被关联对象在R2关联对象定义中的属性名称，如UserR2Role中定义的role属性名
     * @throws Exception 
     */
    protected void updateRelatedR2s(T entity, Serializable[] r2EntityIds, String r2PropertyName, String r2EntityPropertyName) throws Exception {
        try {
            List oldR2s = (List) MethodUtils.invokeExactMethod(entity, "get" + StringUtils.capitalize(r2PropertyName), null);
            if (oldR2s == null) {
                oldR2s = Lists.newArrayList();
                FieldUtils.writeDeclaredField(entity, r2PropertyName, oldR2s, true);
            }
            if ((r2EntityIds == null || r2EntityIds.length == 0)) {
                if (!CollectionUtils.isEmpty(oldR2s)) {
                    oldR2s.clear();
                }
            } else {
                Field r2field = FieldUtils.getField(getEntityClass(), r2PropertyName, true);
                Class r2Class = (Class) (((ParameterizedType) r2field.getGenericType()).getActualTypeArguments()[0]);
                Field entityField = null;
                Field[] fields = r2Class.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType().equals(getEntityClass())) {
                        entityField = field;
                        break;
                    }
                }

                Field r2EntityField = FieldUtils.getField(r2Class, r2EntityPropertyName, true);
                Class r2EntityClass = r2EntityField.getType();

                // 双循环处理需要删除关联的项目
                if (CollectionUtils.isNotEmpty(oldR2s)) {
                    List tobeDleteList = Lists.newArrayList();
                    for (Object r2 : oldR2s) {
                        boolean tobeDlete = true;
                        for (Serializable r2EntityId : r2EntityIds) {
                            Object r2Entity = getEntityManager().find(r2EntityClass, r2EntityId);
                            if (FieldUtils.readDeclaredField(r2, r2EntityPropertyName, true).equals(r2Entity)) {
                                tobeDlete = false;
                                break;
                            }
                        }
                        if (tobeDlete) {
                            tobeDleteList.add(r2);
                        }
                    }
                    oldR2s.removeAll(tobeDleteList);
                }

                // 双循环处理需要新增关联的项目
                for (Serializable r2EntityId : r2EntityIds) {
                    Object r2Entity = getEntityManager().find(r2EntityClass, r2EntityId);
                    boolean tobeAdd = true;
                    if (CollectionUtils.isNotEmpty(oldR2s)) {
                        for (Object r2 : oldR2s) {
                            if (FieldUtils.readDeclaredField(r2, r2EntityPropertyName, true).equals(r2Entity)) {
                                tobeAdd = false;
                                break;
                            }
                        }
                    }
                    if (tobeAdd) {
                        Object newR2 = r2Class.newInstance();
                        FieldUtils.writeDeclaredField(newR2, r2EntityField.getName(), r2Entity, true);
                        FieldUtils.writeDeclaredField(newR2, entityField.getName(), entity, true);
                        oldR2s.add(newR2);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Transactional(readOnly = true)
    public Object findEntity(Class entityClass, Serializable id) {
        return getEntityManager().find(entityClass, id);
    }

    public void detach(Object entity) {
        getEntityManager().detach(entity);
    }
}
