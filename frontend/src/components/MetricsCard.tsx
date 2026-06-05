interface MetricsCardProps {
  title: string
  value: string
  unit: string
  icon: string
  color: 'cyan' | 'red' | 'orange' | 'green' | 'blue'
}

export default function MetricsCard({ title, value, unit, icon, color }: MetricsCardProps) {
  const colorClasses = {
    cyan: 'bg-cyan-900 border-cyan-700 text-cyan-400',
    red: 'bg-red-900 border-red-700 text-red-400',
    orange: 'bg-orange-900 border-orange-700 text-orange-400',
    green: 'bg-green-900 border-green-700 text-green-400',
    blue: 'bg-blue-900 border-blue-700 text-blue-400',
  }

  return (
    <div className={`${colorClasses[color]} border rounded-lg p-6`}>
      <div className="flex justify-between items-start">
        <div>
          <p className="text-gray-400 text-sm mb-1">{title}</p>
          <p className="text-3xl font-bold">{value}</p>
          <p className="text-xs text-gray-500 mt-1">{unit}</p>
        </div>
        <span className="text-3xl">{icon}</span>
      </div>
    </div>
  )
}
