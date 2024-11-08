import { User } from './user.js'
import { PersonalData } from './personalData.js'
import { Category } from './category.js'
import { Spent } from './spent.js'
import { Income } from './income.js'

export const models = [User, PersonalData, Category, Spent, Income]

export function sync() {
    return Promise.all(models.map((model) => model.sync()))
}
