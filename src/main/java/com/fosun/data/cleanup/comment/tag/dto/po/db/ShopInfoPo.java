package com.fosun.data.cleanup.comment.tag.dto.po.db;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zyf
 * @date 2019/03/28
 * 店铺信息表
 */

@Table(name = "t_shop_info")
@Entity
public class ShopInfoPo extends BasePo{

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 所属商场id (关联商场表)
     */
    private Long mallId;

    /**
     * 所属品牌id (关联品牌表)
     */
    private Long brandId;

    /**
     * 楼层信息
     */
    private Long floor;

    /**
     * 人均消费
     */
    private BigDecimal consumptionPer;

    /**
     * 评论数
     */
    private Long comments;

    /**
     * 店铺星级
     */
    private BigDecimal shopLevel;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 店铺经度
     */
    private BigDecimal longitude;

    /**
     * 店铺纬度
     */
    private BigDecimal latitude;

    /**
     * 店铺联系电话
     */
    private String shopTel;

    /**
     * 营业日
     */
    private String businessOpenday;

    /**
     * 营业开始时间
     */
    private String businessStart;

    /**
     * 营业结束时间
     */
    private String businessEnd;

    /**
     * 24小时营业标识 1:是 0:否
     */
    private Byte openAllday;

    /**
     * 整周营业标识 1:是 0:否
     */
    private Byte openAllweek;

    /**
     * 所属区域
     */
    private String parkId;

    /**
     * 所属省份
     */
    private String provinceId;

    /**
     * 所属城市
     */
    private String cityId;

    /**
     * 商场所在行政区 (关联地区表)
     */
    private String citySubId;

    /**
     * 商场所在商圈 (关联商圈表)
     */
    private String districtId;

    /**
     * 一级业态
     */
    private Long flevelTypeId;

    /**
     * 二级业态
     */
    private Long slevelTypeId;

    /**
     * 用户自定义一级业态
     */
    private Long customerFlevelTypeId;

    /**
     * 用户自定义二级业态
     */
    private Long customerSlevelTypeId;



    /**
     * 三级业态
     */
    private Long tlevelTypeId;

    /**
     * 营业状态
     */
    private Integer shopStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 店铺id
     * @return shop_id 店铺id
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 店铺id
     * @param shopId 店铺id
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /**
     * 店铺名称
     * @return shop_name 店铺名称
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 店铺名称
     * @param shopName 店铺名称
     */
    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    /**
     * 所属商场id (关联商场表)
     * @return mall_id 所属商场id (关联商场表)
     */
    public Long getMallId() {
        return mallId;
    }

    /**
     * 所属商场id (关联商场表)
     * @param mallId 所属商场id (关联商场表)
     */
    public void setMallId(Long mallId) {
        this.mallId = mallId;
    }

    /**
     * 所属品牌id (关联品牌表)
     * @return brand_id 所属品牌id (关联品牌表)
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * 所属品牌id (关联品牌表)
     * @param brandId 所属品牌id (关联品牌表)
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * 楼层信息
     * @return floor 楼层信息
     */
    public Long getFloor() {
        return floor;
    }

    /**
     * 楼层信息
     * @param floor 楼层信息
     */
    public void setFloor(Long floor) {
        this.floor = floor;
    }

    /**
     * 人均消费
     * @return consumption_per 人均消费
     */
    public BigDecimal getConsumptionPer() {
        return consumptionPer;
    }

    /**
     * 人均消费
     * @param consumptionPer 人均消费
     */
    public void setConsumptionPer(BigDecimal consumptionPer) {
        this.consumptionPer = consumptionPer;
    }

    /**
     * 评论数
     * @return comments 评论数
     */
    public Long getComments() {
        return comments;
    }

    /**
     * 评论数
     * @param comments 评论数
     */
    public void setComments(Long comments) {
        this.comments = comments;
    }

    /**
     * 店铺星级
     * @return shop_level 店铺星级
     */
    public BigDecimal getShopLevel() {
        return shopLevel;
    }

    /**
     * 店铺星级
     * @param shopLevel 店铺星级
     */
    public void setShopLevel(BigDecimal shopLevel) {
        this.shopLevel = shopLevel;
    }

    /**
     * 店铺地址
     * @return address 店铺地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 店铺地址
     * @param address 店铺地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 店铺经度
     * @return longitude 店铺经度
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * 店铺经度
     * @param longitude 店铺经度
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * 店铺纬度
     * @return latitude 店铺纬度
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * 店铺纬度
     * @param latitude 店铺纬度
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * 店铺联系电话
     * @return shop_tel 店铺联系电话
     */
    public String getShopTel() {
        return shopTel;
    }

    /**
     * 店铺联系电话
     * @param shopTel 店铺联系电话
     */
    public void setShopTel(String shopTel) {
        this.shopTel = shopTel == null ? null : shopTel.trim();
    }

    /**
     * 营业日
     * @return business_openday 营业日
     */
    public String getBusinessOpenday() {
        return businessOpenday;
    }

    /**
     * 营业日
     * @param businessOpenday 营业日
     */
    public void setBusinessOpenday(String businessOpenday) {
        this.businessOpenday = businessOpenday == null ? null : businessOpenday.trim();
    }

    /**
     * 营业开始时间
     * @return business_start 营业开始时间
     */
    public String getBusinessStart() {
        return businessStart;
    }

    /**
     * 营业开始时间
     * @param businessStart 营业开始时间
     */
    public void setBusinessStart(String businessStart) {
        this.businessStart = businessStart == null ? null : businessStart.trim();
    }

    /**
     * 营业结束时间
     * @return business_end 营业结束时间
     */
    public String getBusinessEnd() {
        return businessEnd;
    }

    /**
     * 营业结束时间
     * @param businessEnd 营业结束时间
     */
    public void setBusinessEnd(String businessEnd) {
        this.businessEnd = businessEnd == null ? null : businessEnd.trim();
    }

    /**
     * 24小时营业标识 1:是 0:否
     * @return open_allday 24小时营业标识 1:是 0:否
     */
    public Byte getOpenAllday() {
        return openAllday;
    }

    /**
     * 24小时营业标识 1:是 0:否
     * @param openAllday 24小时营业标识 1:是 0:否
     */
    public void setOpenAllday(Byte openAllday) {
        this.openAllday = openAllday;
    }

    /**
     * 整周营业标识 1:是 0:否
     * @return open_allweek 整周营业标识 1:是 0:否
     */
    public Byte getOpenAllweek() {
        return openAllweek;
    }

    /**
     * 整周营业标识 1:是 0:否
     * @param openAllweek 整周营业标识 1:是 0:否
     */
    public void setOpenAllweek(Byte openAllweek) {
        this.openAllweek = openAllweek;
    }

    /**
     * 所属区域
     * @return park_id 所属区域
     */
    public String getParkId() {
        return parkId;
    }

    /**
     * 所属区域
     * @param parkId 所属区域
     */
    public void setParkId(String parkId) {
        this.parkId = parkId == null ? null : parkId.trim();
    }

    /**
     * 所属省份
     * @return province_id 所属省份
     */
    public String getProvinceId() {
        return provinceId;
    }

    /**
     * 所属省份
     * @param provinceId 所属省份
     */
    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId == null ? null : provinceId.trim();
    }

    /**
     * 所属城市
     * @return city_id 所属城市
     */
    public String getCityId() {
        return cityId;
    }

    /**
     * 所属城市
     * @param cityId 所属城市
     */
    public void setCityId(String cityId) {
        this.cityId = cityId == null ? null : cityId.trim();
    }

    /**
     * 商场所在行政区 (关联地区表)
     * @return city_sub_id 商场所在行政区 (关联地区表)
     */
    public String getCitySubId() {
        return citySubId;
    }

    /**
     * 商场所在行政区 (关联地区表)
     * @param citySubId 商场所在行政区 (关联地区表)
     */
    public void setCitySubId(String citySubId) {
        this.citySubId = citySubId == null ? null : citySubId.trim();
    }

    /**
     * 商场所在商圈 (关联商圈表)
     * @return district_id 商场所在商圈 (关联商圈表)
     */
    public String getDistrictId() {
        return districtId;
    }

    /**
     * 商场所在商圈 (关联商圈表)
     * @param districtId 商场所在商圈 (关联商圈表)
     */
    public void setDistrictId(String districtId) {
        this.districtId = districtId == null ? null : districtId.trim();
    }

    /**
     * 一级业态
     * @return flevel_type_id 一级业态
     */
    public Long getFlevelTypeId() {
        return flevelTypeId;
    }

    /**
     * 一级业态
     * @param flevelTypeId 一级业态
     */
    public void setFlevelTypeId(Long flevelTypeId) {
        this.flevelTypeId = flevelTypeId;
    }

    /**
     * 二级业态
     * @return slevel_type_id 二级业态
     */
    public Long getSlevelTypeId() {
        return slevelTypeId;
    }

    /**
     * 二级业态
     * @param slevelTypeId 二级业态
     */
    public void setSlevelTypeId(Long slevelTypeId) {
        this.slevelTypeId = slevelTypeId;
    }

    /**
     * 三级业态
     * @return tlevel_type_id 三级业态
     */
    public Long getTlevelTypeId() {
        return tlevelTypeId;
    }

    /**
     * 三级业态
     * @param tlevelTypeId 三级业态
     */
    public void setTlevelTypeId(Long tlevelTypeId) {
        this.tlevelTypeId = tlevelTypeId;
    }

    /**
     * 营业状态
     * @return shop_status 营业状态
     */
    public Integer getShopStatus() {
        return shopStatus;
    }

    /**
     * 营业状态
     * @param shopStatus 营业状态
     */
    public void setShopStatus(Integer shopStatus) {
        this.shopStatus = shopStatus;
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     * @return update_time 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    public Long getCustomerFlevelTypeId() {
        return customerFlevelTypeId;
    }

    public void setCustomerFlevelTypeId(Long customerFlevelTypeId) {
        this.customerFlevelTypeId = customerFlevelTypeId;
    }

    public Long getCustomerSlevelTypeId() {
        return customerSlevelTypeId;
    }

    public void setCustomerSlevelTypeId(Long customerSlevelTypeId) {
        this.customerSlevelTypeId = customerSlevelTypeId;
    }
}