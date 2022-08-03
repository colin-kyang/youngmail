package com.example.youngmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.example.youngmall.product.entity.vo.Catalog2Vo;
import com.example.youngmall.product.service.CategoryBrandRelationService;
import io.renren.common.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.CategoryDao;
import com.example.youngmall.product.entity.CategoryEntity;
import com.example.youngmall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 商品 类别-树状结构查询
     *
     * @return
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        //查询所有分类数据
        List<CategoryEntity> list = baseMapper.selectList(null);

        //1. 为所有分类建立索引
        Map<Long, CategoryEntity> mapp = new HashMap<>();
        for (CategoryEntity e : list) {
            mapp.put(e.getCatId(), e);
            if (e.getCatLevel() <= 2) {
                e.setChildren(new ArrayList<>());
            }
        }
        //2. 为每一个非第一层级设置其子分类
        for (CategoryEntity e : list) {
            //此项目非第一层级
            if (e.getParentCid() != 0) {
                CategoryEntity parent = mapp.get(e.getParentCid());
                if (parent != null) {
                    parent.addchildren(e);
                }
            }
        }
        //3. 找到第一层级，并设置其子类列表
        List<CategoryEntity> result = new ArrayList<>();
        for (CategoryEntity e : list) {
            if (e.getParentCid() == 0) {
                result.add(e);
            }
            if (e.getChildren() != null && e.getChildren().size() != 0) {
                e.getChildren().sort(new Comparator<CategoryEntity>() {
                    @Override
                    public int compare(CategoryEntity o1, CategoryEntity o2) {
                        return o1.getSort() - o2.getSort();
                    }
                });
            }

        }
        result.sort(new Comparator<CategoryEntity>() {
            @Override
            public int compare(CategoryEntity o1, CategoryEntity o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        return result;
    }


    /**
     * 删除某一级菜单及其子菜单
     *
     * @param catIds
     */
    @Override
    public void removeCategoryByIds(Long[] catIds) {
        Map<Long, CategoryEntity> categoryEntityMap = new HashMap<>();
        List<Long> deleteList = new ArrayList<>();
        //最多扫两边算法
        for (Long id : catIds) {
            deleteList.add(id);
        }
        List<CategoryEntity> dict = baseMapper.selectList(null);
        //第一遍，保证将需要删除的二级菜单搜索完毕，第二遍保证将三级菜单搜索完毕
        for (int i = 0; i < 2; i++) {
            for (CategoryEntity e : dict) {
                //若当前条目尚未加入列表，且其父菜单在列表中
                if (!deleteList.contains(e.getCatId()) && deleteList.contains(e.getParentCid())) {
                    deleteList.add(e.getCatId());
                }
            }
        }
        if (deleteList.size() != 0) {
            baseMapper.deleteBatchIds(deleteList);
        }
    }

    /**
     * 发现编号为catelogIdPath 的三级种类的 三级路径
     *
     * @param catelogIdPath
     * @return
     */
    @Override
    public Long[] findCategoryPath(Long catelogIdPath) {
        List<Long> paths = new ArrayList<>();
        paths.add(catelogIdPath);
        //select parent_cid from where cat_id = catelogIdPath
        CategoryEntity category = baseMapper.selectById(catelogIdPath);
        Long parent_id = category.getParentCid();
        CategoryEntity p1 = baseMapper.selectById(parent_id);
        paths.add(p1.getCatId());
        paths.add(p1.getParentCid());
        //列表翻转
        Collections.reverse(paths);
        //列表可以直接转换为数组
        return paths.toArray(new Long[paths.size()]);
    }

    @Override
    public List<CategoryEntity> getLevel1Category() {
        QueryWrapper<CategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_cid", 0);
        return baseMapper.selectList(wrapper);
    }


    /**
     * 从数据库查询并封装分类数据
     * 首页 -> 二级菜单列表json
     *
     * @return
     */
    @Override
    public Map<String, List<Catalog2Vo>> getCatelogJson() {
        // 只要是同一把锁就能锁住这个锁的所有线程
        // 1、synchronized(this)，springboot 所有组件在容器中都是单例的。相当于并发请求都是同一把锁
            // 得到锁以后，需要在缓存中确定一次，如果没有才需要继续查询
            String catalogJson = redisTemplate.opsForValue().get("catalogJSON");
            if (!StringUtils.isEmpty(catalogJson)) {
                Map<String, List<Catalog2Vo>> result =  JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>(){});
                return result;
            }
            // 1. 查出所有1级分类
            List<CategoryEntity> category1LevelList = getParent_cid(0L);
            // 2. 分装数据
            Map<String, List<Catalog2Vo>> parent_cid = category1LevelList.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                // 2.1 查询该1级分类的所有二级分类
                List<CategoryEntity> category2LevelList = getParent_cid(v.getCatId());
                List<Catalog2Vo> catalog2Vos = null;
                if (category2LevelList != null) {
                    catalog2Vos = category2LevelList.stream().map(item -> {
                        //寻找当前二级分类的三级分类
                        List<Catalog2Vo.catalog3Vo> catalog3Vos = null;
                        List<CategoryEntity> category3LevelentityList = getParent_cid(item.getCatId());
                        // 3级分类
                        if (category3LevelentityList != null) {
                            catalog3Vos = category3LevelentityList.stream().map(level3 -> {
                                return new Catalog2Vo.catalog3Vo(item.getCatId().toString(), level3.getCatId().toString(), level3.getName());
                            }).collect(Collectors.toList());
                        }
                        return new Catalog2Vo(v.getCatId().toString(), catalog3Vos, item.getCatId().toString(), item.getName());
                    }).collect(Collectors.toList());
                }
                return catalog2Vos;
            }));
            catalogJson = JSON.toJSONString(parent_cid);
            // 保存日期为1天
            redisTemplate.opsForValue().set("catalogJson", catalogJson, 1, TimeUnit.DAYS);
            return parent_cid;
    }

    /**
     * 本地锁
     * @return
     */
    @Override
    public Map<String, List<Catalog2Vo>> getCatelogJsonFromRedis() {
        // 给缓存中存放json字符串，拿出的json字符串需要逆转为能用的数据类型 【序列化与反序列化】
        // 缓存中存放的是json字符串
        // json 跨语言、跨平台
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        // 只要是同一把锁，就能锁住这个锁的所有线程
        if (StringUtils.isEmpty(catalogJson)) {
            synchronized(this) {
                System.out.println("缓存未命中，查询数据库");
                // 缓存中没有，则查询数据库
                Map<String, List<Catalog2Vo>> catalogJsonFromDb = getCatelogJson();
                return  catalogJsonFromDb;
            }
        }
        System.out.println("缓存命中，查询数据库");
        Map<String, List<Catalog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {
        });
        return result;
    }

    /**
     * 分布式锁
     * @return
     */
    @Override
    public Map<String, List<Catalog2Vo>> getCatelogJsonFromRedisWithRedisLock() throws InterruptedException {
        String catalogJson = redisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            //1. 占用分布式锁，redis抢锁
            //2. 设置过期时间，避免死锁问题
            String uuid = UUID.randomUUID().toString();
            Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock",uuid,30,TimeUnit.SECONDS);
            // 加锁成功
            if (lock) {
                System.out.println("获取分布式锁成功");
                // 缓存中没有，则查询数据库
                Map<String, List<Catalog2Vo>> catalogJsonFromDb = getCatelogJson();
                //解锁 : 可能过期了
                // 原子操作，执行脚本
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                Integer result = redisTemplate.execute(new DefaultRedisScript<Integer>(script,Integer.class),Arrays.asList("lock"),uuid);
                return  catalogJsonFromDb;
            } else {
                System.out.println("获取分布式锁失败，等待重试");
                // 加锁失败...重试
                //休眠100ms
                //避免反复尝试栈溢出
                Thread.sleep(200);
                return getCatelogJsonFromRedisWithRedisLock(); //自旋方式
            }

        }
        System.out.println("缓存命中，查询数据库");
        Map<String, List<Catalog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {
        });
        return result;
    }

    /**
     * 查询父分类为parentCid的菜单
     *
     * @param parentCid
     * @return
     */
    private List<CategoryEntity> getParent_cid(Long parentCid) {
        List<CategoryEntity> list = baseMapper.selectList(null);
        List<CategoryEntity> result = list.stream().filter(item -> {
            return item.getParentCid() == parentCid;
        }).collect(Collectors.toList());
        return result;
    }

    //1.锁的名字。锁的粒度，越细越快

    /**
     * 缓存数据一致性：与数据库中数据保持一致
     * @return
     * @throws InterruptedException
     */
    @Override
    public Map<String, List<Catalog2Vo>> getCatelogJsonFromRedisWithRedissonLock() throws InterruptedException {
        RLock lock = redissonClient.getLock("CatalogJson-lock");
        lock.lock();
        Map<String,List<Catalog2Vo>> dataFromDb;
        try {
            //如果源数据修改
            dataFromDb = getCatelogJson();
        } finally {
            lock.unlock();
        }
        return dataFromDb;
    }
}