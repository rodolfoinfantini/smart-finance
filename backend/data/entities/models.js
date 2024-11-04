import { User } from './user.js'
import { PersonalData } from './personalData.js'

export const models = [User, PersonalData]

export function sync() {
    return Promise.all(
        models.map((model) =>
            model.sync({
                alter: true,
            }),
        ),
    )
}
