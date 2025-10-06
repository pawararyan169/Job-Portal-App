const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');

const authRoute = require('./routes/auth');
const jobsRoute = require('./routes/jobs');

const app = express();
const PORT = process.env.PORT || 5000;

// Replace this with your MongoDB URI
const MONGO_URI = 'mongodb://localhost:27017/jobportal';

mongoose.connect(MONGO_URI, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
}).then(() => {
  console.log('MongoDB connected');
}).catch((err) => {
  console.error('MongoDB connection error:', err);
});

app.use(cors());
app.use(express.json());

// Routes
app.use('/api/auth', authRoute);
app.use('./api/jobs', jobsRoute);

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});