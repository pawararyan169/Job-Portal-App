const express = require('express');
const router = express.Router();

// Example: Get all jobs (You can expand with real data and DB integration later)
router.get('/', (req, res) => {
  res.json({ message: "Get all jobs endpoint working" });
});

module.exports = router;