import { DataTypes } from '@sequelize/core'
import { v4 as uuid } from 'uuid'
import { sequelize } from '../connection.js'
import { User } from './user.js'

const PersonalData = sequelize.define('PersonalData', {
    id: {
        type: DataTypes.STRING(36),
        primaryKey: true,
        defaultValue: () => uuid(),
    },
    fullName: {
        type: DataTypes.STRING,
        allowNull: false,
    },
    age: {
        type: DataTypes.INTEGER,
        allowNull: false,
    },
    occupation: {
        type: DataTypes.STRING,
        allowNull: false,
    },
    mensalIncome: {
        type: DataTypes.DECIMAL(10, 2),
        allowNull: false,
    },
})
PersonalData.belongsTo(User)

function toDto(personalData) {
    return {
        fullName: personalData.fullName,
        age: personalData.age,
        occupation: personalData.occupation,
        mensalIncome: personalData.mensalIncome,
    }
}

export { PersonalData, toDto }
