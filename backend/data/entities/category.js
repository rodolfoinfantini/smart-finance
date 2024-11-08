import { DataTypes } from '@sequelize/core'
import { v4 as uuid } from 'uuid'
import { sequelize } from '../connection.js'

const Category = sequelize.define('Category', {
    id: {
        type: DataTypes.STRING(36),
        primaryKey: true,
        defaultValue: () => uuid(),
    },
    name: {
        type: DataTypes.STRING,
        allowNull: false,
    },
})

export { Category }
