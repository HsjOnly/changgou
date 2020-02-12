package com.itheima.changgou.goods.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.changgou.util.IdWorker;
import com.itheima.changgou.goods.dao.CategoryDao;
import com.itheima.changgou.goods.dao.SkuDao;
import com.itheima.changgou.goods.dao.SpuDao;
import com.itheima.changgou.goods.pojo.Category;
import com.itheima.changgou.goods.pojo.Goods;
import com.itheima.changgou.goods.pojo.Sku;
import com.itheima.changgou.goods.pojo.Spu;
import com.itheima.changgou.goods.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigInteger;
import java.util.*;

@Service
@Transactional
public class SpuServiceImpl implements SpuService {

    @Autowired(required = false)
    private SpuDao spuDao;

    @Autowired(required = false)
    private CategoryDao categoryDao;

    @Autowired(required = false)
    private SkuDao skuDao;

    @Autowired
    // 添加spu的ID(雪花算法)
    private IdWorker idWorker;

    @Override
    public void insetOrUpdateSpu(Goods goods) throws Exception {
        // 获取spu
        Spu spu = goods.getSpu();

        // 未提供spu的情况
        if (spu == null) {
            throw new RuntimeException("提供的数据不全，请修改后重试");
        }

        // 当传过来的SPU没有ID时，执行新增，否则执行修改
        if (spu.getId() == null) {
            // 新增spu
            // 添加spu的ID(雪花算法)
            spu.setId(idWorker.nextId());

            // 未上架
            spu.setIsMarketable("0");

            // 未删除
            spu.setIsDelete("0");

            // 未审核
            spu.setStatus("0");

            // 将SPU存入数据库
            spuDao.insert(spu);

        } else {
            // 修改spu
            // 若spu已经删除则不可修改
            Spu spuInDataBase = spuDao.selectByPrimaryKey(spu.getId());

            // 查询ID没有结果
            if (spuInDataBase == null) {
                throw new RuntimeException("你所修改的商品不存在");
            }

            // 查询的spu已经被逻辑删除
            if ("1".equals(spuInDataBase.getIsDelete())){
                throw new RuntimeException("商品已经删除，请从回收站中恢复后再修改");
            }

            // 未上架
            spu.setIsMarketable("0");

            // 未审核
            spu.setStatus("0");

            // 将更新后的Spu存入数据库
            spuDao.updateByPrimaryKey(spu);

            // 删除原有的Sku（商品状态=3）
            Example example = new Example(Sku.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("spuId", spu.getId());
            Sku sku = new Sku();
            sku.setStatus("3");
            skuDao.updateByExampleSelective(sku, example);
        }

        // 获取spu下的sku
        List<Sku> skus = goods.getSkuList();

        // 处理每个sku
        for (Sku sku : skus) {
            // 设置sku的id
            sku.setId(idWorker.nextId());

            // 设置sku名(spu名+规格拼装)
            // 先拼装spu名
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(spu.getName());
            stringBuffer.append(" ");

            // 再拼装sku属性
            // 取出sku中存有所有属性的Json，并转换为Map，方便操作
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> specs = objectMapper.readValue(sku.getSpec(), HashMap.class);

            // 遍历Map，获取每个属性值
            Set<String> specNames = specs.keySet();
            for (String specName : specNames) {
                String specValue = specs.get(specName);
                stringBuffer.append(specValue);
                stringBuffer.append(" ");
            }
            sku.setName(stringBuffer.toString());

            // 设置创建时间
            sku.setCreateTime(new Date());

            // 设置修改时间
            sku.setUpdateTime(new Date());

            // 设置所属spu的id
            sku.setSpuId(spu.getId());

            // 设置类目名称
            Category category = categoryDao.selectByPrimaryKey(sku.getCategoryId());
            sku.setCategoryName(category.getName());

            // 设置商品状态为正常
            sku.setStatus("1");

            // 将sku存入数据库
            // skuDao.insertSelective(sku);
        }
        // 将多条sku存入数据库
        skuDao.insertSkuList(skus);

    }

    @Override
    public Goods getSpuBySpuID(BigInteger spuID) {
        // 查询SPU
        Spu spu = spuDao.selectByPrimaryKey(spuID);
        // 查询属于spu且状态为正常的的sku
        Example example = new Example(Sku.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("spuId", spuID);
        criteria.andEqualTo("status", 1);
        List<Sku> skus = skuDao.selectByExample(example);

        // 封装为Good
        Goods goods = new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skus);
        return goods;
    }

    @Override
    public void deleteSpuBySpuID(BigInteger spuID) {
        Spu spuInDataBase = spuDao.selectByPrimaryKey(spuID);

        // 查询ID没有结果
        if (spuInDataBase == null) {
            throw new RuntimeException("你所逻辑删除的商品不存在");
        }

        // 删除spu下的sku(商品状态=3)
//        Example example = new Example(Sku.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("spuId", spuID);
//        Sku sku = new Sku();
//        sku.setStatus("3");

//        skuDao.updateByExampleSelective(sku, example);

        // 删除spu(删除状态=1)
        Spu spu = new Spu();
        spu.setId(spuID.longValue());
        spu.setIsDelete("1");
        spuDao.updateByPrimaryKeySelective(spu);

    }

    @Override
    public void recoverSpuBySpuId(BigInteger spuId) {

        Spu spuInDataBase = spuDao.selectByPrimaryKey(spuId);

        // 查询ID没有结果
        if (spuInDataBase == null) {
            throw new RuntimeException("你所恢复的商品不存在");
        }

        // 查询的spu未被逻辑删除
        if (!"1".equals(spuInDataBase.getIsDelete())){
            throw new RuntimeException("商品未被逻辑删除");
        }

        spuInDataBase.setIsDelete("0");
        spuDao.updateByPrimaryKeySelective(spuInDataBase);
    }

    @Override
    public void foreverDeleteSpuBySpuId(BigInteger spuId) {

        Spu spuInDataBase = spuDao.selectByPrimaryKey(spuId);

        // 查询ID没有结果
        if (spuInDataBase == null) {
            throw new RuntimeException("你所删除的商品不存在");
        }

        // 查询的spu未被逻辑删除
        if (!"1".equals(spuInDataBase.getIsDelete())){
            throw new RuntimeException("商品未被逻辑删除");
        }

        spuDao.deleteByPrimaryKey(spuId);
    }

    @Override
    public int putawaySpusBySpuIds(BigInteger[] spuIds) {
        // 设置更新条件(id、未删除、审核通过）
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(spuIds));
        criteria.andEqualTo("isDelete", "0");
        criteria.andEqualTo("status", "1");

        Spu spu = new Spu();
        spu.setIsMarketable("1");
        return spuDao.updateByExampleSelective(spu, example);
    }

    @Override
    public int offShelvesSpusBySpuIds(BigInteger[] spuIds) {
        // 更新条件(id、未删除、已上架)
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDelete", "0");
        criteria.andEqualTo("isMarketable", "1");

        Spu spu = new Spu();
        spu.setIsMarketable("0");
        return spuDao.updateByExampleSelective(spu, example);
    }

    @Override
    public Spu getSpuById(Long spuId) {
        return spuDao.selectByPrimaryKey(spuId);
    }


}
