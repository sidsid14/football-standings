# Use a Node.js image to build the React application
FROM node:18-alpine AS builder

# Set the working directory in the container
WORKDIR /app

# Copy package.json and package-lock.json (if you have one)
COPY package.json ./
COPY package-lock.json ./

# Install dependencies
RUN npm install

# Copy the rest of the React application code
COPY . .

# Build the React application for production
RUN npm run build

# Use a lightweight Nginx image to serve the static assets
FROM nginx:alpine

# Copy the built React application from the 'builder' stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Expose the port that Nginx will listen on (usually 80)
EXPOSE 80

# Optional: Custom Nginx configuration (if needed)
# COPY nginx.conf /etc/nginx/nginx.conf

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
