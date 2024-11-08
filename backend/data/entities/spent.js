import { DataTypes } from '@sequelize/core'
import { sequelize } from '../connection.js'
import { v4 as uuid } from 'uuid'
import { User } from './user.js'
import { Category } from './category.js'

const Spent = sequelize.define('Spent', {
    id: {
        type: DataTypes.STRING(36),
        primaryKey: true,
        defaultValue: () => uuid(),
    },
    value: {
        type: DataTypes.DECIMAL(10, 2),
        allowNull: false,
    },
})
Spent.belongsTo(User)
Spent.belongsTo(Category)

export { Spent }
