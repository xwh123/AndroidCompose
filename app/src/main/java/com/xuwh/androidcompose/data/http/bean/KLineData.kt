package com.xuwh.androidcompose.data.http.bean

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.data.http.bean
 * @ClassName:      KLineData
 * @Description:    类作用描述
 * @Author:         xuwh
 * @CreateDate:     2025/4/8 下午9:41
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/4/8 下午9:41
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
/**
 * K线数据
 * @property open Float 开盘价
 * @property high Float 最高价
 * @property low Float 最低价
 * @property close Float 收盘价
 * @constructor
 */
data class KLineData(      val timestamp: Long,  // 时间戳
                           val open: Float,      // 开盘价
                           val high: Float,      // 最高价
                           val low: Float,       // 最低价
                           val close: Float,     // 收盘价
                           val volume: Float? = null // 成交量(可选)
)

// 时分图数据点
data class TickData(
    val timestamp: Long,  // 时间戳
    val price: Float,     // 价格
    val volume: Float     // 成交量
)