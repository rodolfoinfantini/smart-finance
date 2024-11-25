import usersRouter from './usersRouter.js'
import personalDataRouter from './personalDataRouter.js'
import spentsRouter from './spentsRouter.js'
import eventRouter from './eventRouter.js'
import balanceRouter from './balanceRouter.js'
import incomesRouter from './incomesRouter.js'

export default function initRoutes(app) {
    app.use('/users', usersRouter)
    app.use('/personal-data', personalDataRouter)
    app.use('/spents', spentsRouter)
    app.use('/incomes', incomesRouter)
    app.use('/balance', balanceRouter)
    app.use('/event', eventRouter)

    app.get('*', (_, res) => {
        res.status(404).send('Not Found');
    });

    app.use((err, _, res) => {
        console.error(err)
        try {
            res.status(500).json({ message: 'Something went wrong' })
        } catch (error) {
            console.error(error)
        }
    })
}
