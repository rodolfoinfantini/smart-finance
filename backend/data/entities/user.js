import { DataTypes } from '@sequelize/core'
import { v4 as uuid } from 'uuid'
import { sequelize } from '../connection.js'

export const User = sequelize.define('User', {
    id: {
        type: DataTypes.STRING(36),
        primaryKey: true,
        defaultValue: () => uuid(),
    },
    email: {
        type: DataTypes.STRING,
        allowNull: false,
    },
    firstName: {
        type: DataTypes.STRING,
        allowNull: false,
    },
    lastName: {
        type: DataTypes.STRING,
        allowNull: true,
    },
    password: {
        type: DataTypes.STRING,
        allowNull: false,
    },
})

export function toDto(user) {
    return {
        id: user.id,
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName,
        updatedAt: user.updatedAt,
        createdAt: user.createdAt,
    }
}
