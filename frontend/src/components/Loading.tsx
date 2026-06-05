export default function Loading() {
  return (
    <div className="flex items-center justify-center min-h-screen bg-background">
      <div className="text-center">
        <div className="inline-block">
          <div className="relative w-16 h-16">
            <svg className="w-full h-full animate-spin" viewBox="0 0 100 100">
              <circle
                cx="50"
                cy="50"
                r="40"
                fill="none"
                stroke="currentColor"
                strokeWidth="4"
                className="text-surface-container-highest"
              />
              <circle
                cx="50"
                cy="50"
                r="40"
                fill="none"
                stroke="currentColor"
                strokeWidth="4"
                strokeDasharray="60 100"
                strokeLinecap="round"
                className="text-primary"
              />
            </svg>
          </div>
        </div>
        <p className="mt-4 text-text-muted font-body-md">Loading security data...</p>
      </div>
    </div>
  )
}
