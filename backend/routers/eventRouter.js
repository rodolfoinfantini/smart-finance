import { Router } from 'express'
import Server from '../server/server.js'

const eventRouter = Router()

eventRouter.post('/:event', async (req, res, next) => {
    try {
        const event = req.params.event
        const data = await Server().sendEvent(event, req.body)
        res.json(data)
    } catch (error) {
        next(error)
    }
})

export default eventRouter
