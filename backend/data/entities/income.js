import { DataTypes } from '@sequelize/core'
import { sequelize } from '../connection.js'
import { v4 as uuid } from 'uuid'
import { User } from './user.js'

const Income = sequelize.define('Income', {
    id: {
        type: DataTypes.STRING(36),
        primaryKey: true,
        defaultValue: () => uuid(),
    },
    value: {
        type: DataTypes.DECIMAL(10, 2),
        allowNull: false,
    },
    description: {
        type: DataTypes.STRING,
    },
})
Income.belongsTo(User)

function toDto(income) {
    return {
        id: income.id,
        value: +income.value,
        description: income.description,
        createdAt: income.createdAt,
    }
}

export { Income, toDto }
