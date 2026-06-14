import React from 'react';

const Loader = ({ size = 'md', className = '', label = 'Loading...' }) => {
  const sizeClasses = {
    sm: 'w-6 h-6 border-2',
    md: 'w-10 h-10 border-3',
    lg: 'w-16 h-16 border-4',
  };

  return (
    <div className={`flex flex-col items-center justify-center p-6 ${className}`}>
      <div
        className={`${sizeClasses[size]} border-slate-700 border-t-brand-500 rounded-full animate-spin border-glow`}
      />
      {label && <p className="text-sm text-slate-400 mt-4 animate-pulse">{label}</p>}
    </div>
  );
};

export const PageLoader = () => (
  <div className="flex items-center justify-center min-h-[50vh] w-full">
    <Loader size="lg" label="Preparing your environment..." />
  </div>
);

export const SkeletonLoader = ({ count = 3, className = '' }) => (
  <div className={`space-y-3 animate-pulse ${className}`}>
    {Array.from({ length: count }).map((_, idx) => (
      <div key={idx} className="h-4 bg-slate-800 rounded-md w-full" />
    ))}
  </div>
);

export default Loader;
