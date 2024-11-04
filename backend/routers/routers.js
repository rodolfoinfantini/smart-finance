import usersRouter from './usersRouter.js'
import personalDataRouter from './personalDataRouter.js'
import pingRouter from './pingRouter.js'

export default function initRoutes(app) {
    app.use('/users', usersRouter)
    app.use('/personal-data', personalDataRouter)
    app.use('/ping', pingRouter)
}
