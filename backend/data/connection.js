import { Sequelize } from '@sequelize/core'
import { MySqlDialect } from '@sequelize/mysql'

export const sequelize = new Sequelize({
    dialect: MySqlDialect,
    database: process.env.DB_DATABASE ?? 'pi',
    user: process.env.DB_USER ?? 'root',
    password: process.env.DB_PASS ?? 'root',
    host: process.env.DB_HOST ?? 'localhost',
    port: +(process.env.DB_PORT ?? 3306),
})
