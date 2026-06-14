import React from 'react';
import { User, Cpu, Sparkles } from 'lucide-react';

const ChatBubble = ({ message, isAi = false, timestamp }) => {
  const alignClass = isAi ? 'justify-start' : 'justify-end';
  const bubbleBg = isAi
    ? 'bg-dark-800 text-slate-100 rounded-bl-none border border-dark-700/60'
    : 'bg-gradient-to-tr from-brand-600 to-indigo-600 text-white rounded-br-none shadow-md shadow-brand-500/10';

  const avatar = isAi ? (
    <div className="w-8 h-8 rounded-lg bg-indigo-500/10 border border-indigo-500/30 flex items-center justify-center text-indigo-400">
      <Cpu className="w-4 h-4" />
    </div>
  ) : (
    <div className="w-8 h-8 rounded-lg bg-brand-500/10 border border-brand-500/30 flex items-center justify-center text-brand-400">
      <User className="w-4 h-4" />
    </div>
  );

  return (
    <div className={`flex items-start space-x-3 w-full mb-6 ${alignClass}`}>
      {isAi && avatar}
      
      <div className="flex flex-col max-w-[75%] space-y-1">
        {/* Name and Time */}
        <div className={`flex items-center space-x-2 text-xxs text-slate-400 ${!isAi ? 'justify-end' : ''}`}>
          <span className="font-semibold text-slate-300">
            {isAi ? 'AI Interviewer' : 'You'}
          </span>
          <span>•</span>
          <span>{timestamp || 'Just now'}</span>
        </div>

        {/* Bubble */}
        <div className={`px-4 py-3 rounded-2xl text-sm leading-relaxed whitespace-pre-wrap ${bubbleBg}`}>
          {message}
        </div>
      </div>

      {!isAi && avatar}
    </div>
  );
};

export default ChatBubble;
