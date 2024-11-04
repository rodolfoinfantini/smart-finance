import usersRouter from './usersRouter.js'
import personalDataRouter from './personalDataRouter.js'

export default function initRoutes(app) {
    app.use('/users', usersRouter)
    app.use('/personal-data', personalDataRouter)
}
