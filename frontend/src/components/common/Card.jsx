import React from 'react';

const Card = ({
  children,
  className = '',
  hover = false,
  onClick,
  ...props
}) => {
  const hoverClass = hover ? 'glass-card-hover cursor-pointer' : '';
  return (
    <div
      onClick={onClick}
      className={`glass-card rounded-2xl p-6 ${hoverClass} ${className}`}
      {...props}
    >
      {children}
    </div>
  );
};

export default Card;
