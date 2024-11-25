FROM node:23.3.0-alpine

WORKDIR /app

COPY package.json .

RUN apk add --no-cache curl

RUN npm install

COPY . .

EXPOSE 3000

CMD ["npm", "start"]
