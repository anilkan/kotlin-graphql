package xyz.anilkan.kotlin.model

import xyz.anilkan.kotlin.repository.MovementItemRepository
import xyz.anilkan.kotlin.util.transactionEnvironment

interface BaseModel {
    val id: Int
}

data class Safe(
    override val id: Int,
    val code: String,
    val name: String,
    val balance: Double
) : BaseModel

data class Firm(
    override val id: Int,
    val name: String
) : BaseModel

enum class MovementType {
    EXPENSE, INCOME
}

interface Movement : BaseModel {
    val type: MovementType
    val from: Any
    val items: List<MovementItem>
}

data class Expense(override val id: Int, override val from: Safe) : Movement {
    override val type: MovementType = MovementType.EXPENSE
    override val items: List<MovementItem> by lazy {
        transactionEnvironment {
            MovementItemRepository.getElementsByMovementId(
                id
            )
        }
    }
}

data class Income(override val id: Int, override val from: Firm) : Movement {
    override val type: MovementType = MovementType.INCOME
    override val items: List<MovementItem> by lazy {
        transactionEnvironment {
            MovementItemRepository.getElementsByMovementId(
                id
            )
        }
    }
}

data class MovementItem(
    override val id: Int,
    val movement: Movement,
    val item: String,
    val amount: Float,
    val itemPrice: Float,
    val itemTotalPrice: Float
) : BaseModel