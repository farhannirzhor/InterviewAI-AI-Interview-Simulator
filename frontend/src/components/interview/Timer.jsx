import React, { useState, useEffect } from 'react';
import { Clock } from 'lucide-react';

const Timer = ({ isRunning = true }) => {
  const [seconds, setSeconds] = useState(0);

  useEffect(() => {
    let interval = null;
    if (isRunning) {
      interval = setInterval(() => {
        setSeconds((prev) => prev + 1);
      }, 1000);
    } else {
      clearInterval(interval);
    }
    return () => clearInterval(interval);
  }, [isRunning]);

  const formatTime = (totalSeconds) => {
    const mins = Math.floor(totalSeconds / 60);
    const secs = totalSeconds % 60;
    return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
  };

  return (
    <div className="flex items-center space-x-2 text-slate-300 font-mono text-sm bg-dark-800 border border-dark-700/60 rounded-xl px-3.5 py-2 shadow-inner">
      <Clock className="w-4 h-4 text-brand-400 animate-pulse-slow" />
      <span>{formatTime(seconds)}</span>
    </div>
  );
};

export default Timer;
