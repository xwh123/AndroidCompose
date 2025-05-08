package com.xuwh.androidcompose.base.effect

import java.util.UUID

/**
 *
 * @ProjectName:    AndroidCompose
 * @Package:        com.xuwh.androidcompose.base.effect
 * @ClassName:      UIEffect
 * @Description:    一次性操作
 * @Author:         xuwh
 * @CreateDate:     2025/3/22 下午1:31
 * @UpdateUser:     更新者
 * @UpdateDate:     2025/3/22 下午1:31
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */
sealed class UIEffect {
    abstract val key: String

    data class SingleShotEffect(
        override val key: String = UUID.randomUUID().toString(),
        val block: suspend () -> Unit
    ) : UIEffect()

    data class PermanentEffect(
        override val key: String = UUID.randomUUID().toString(),
        val block: () -> Unit
    ) : UIEffect()
}